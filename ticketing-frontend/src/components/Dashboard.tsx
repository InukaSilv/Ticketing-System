import React, { useState, useEffect } from "react";
import NavigationBar from "./NavigationBar";
import EventLogs from "./EventLogs";
import ConfigurationCard from "./ConfigurationCard";
import CreateEventForm from "./CreateEventForm";
import API_BASE_URL from "../apiconfig";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';


const Dashboard: React.FC = () => {
  const [showModal, setShowModal] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<string | null>(null);
  const [events, setEvents] = useState<EventConfig[]>([]);
  const [ticketCount, setTicketCount] = useState("");
  const [logs, setLogs] = useState<string[]>([]);

  type EventConfig = {
    id?: number;
    eventName: string;
    vendorRate: number;
    customerRate: number;
    maxCapacity: number;
    ticketPool?: number; 
  };

  useEffect(() => {
    if (!selectedEvent) return;
    const selectedEventData = events.find((event) => event.eventName === selectedEvent);
    if (!selectedEventData || !selectedEventData.id) return;

    const client = new Client({
        brokerURL: "ws://localhost:8080/ws",
        webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
        debug: (str) => console.log(str),
        reconnectDelay: 5000,
    });

    client.onConnect = () => {
        console.log(`Subscribing to logs for event ID: ${selectedEventData.id}`);
        client.subscribe(`/topic/logs/${selectedEventData.id}`, (message) => {
            console.log("Received log from WebSocket:", message.body);
            setLogs((prevLogs) => [...prevLogs, message.body]);
        });
    };

    client.activate();

    return () => {
        client.deactivate();
    };
}, [selectedEvent, events]);

  
  

  // Fetch tickets periodically
  useEffect(() => {
    const fetchTickets = async () => {
      if (!selectedEvent) return;

      const selectedEventData = events.find((event) => event.eventName === selectedEvent);
      if (!selectedEventData || !selectedEventData.id) return;

      try {
        const response = await fetch(`${API_BASE_URL}/event/${selectedEventData.id}/tickets`);
        if (response.ok) {
          const tickets = await response.json(); // Assuming the backend returns an array of tickets
          setEvents((prevEvents) =>
            prevEvents.map((event) =>
              event.id === selectedEventData.id
                ? { ...event, ticketPool: tickets.length } // Update ticket pool
                : event
            )
          );
        } else {
          console.error(`Failed to fetch tickets for event ID: ${selectedEventData.id}`);
        }
      } catch (error) {
        console.error("Error fetching tickets:", error);
      }
    };

    const interval = setInterval(fetchTickets, 700); // Fetch tickets every second
    return () => clearInterval(interval);
  }, [selectedEvent, events]);
  
  

  const handleSaveEvent = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
  
    const formData = new FormData(e.target as HTMLFormElement);
    const newEvent: EventConfig = {
      eventName: formData.get("eventName") as string,
      maxCapacity: parseInt(formData.get("maxCapacity") as string, 10),
      customerRate: parseInt(formData.get("customerRate") as string, 10),
      vendorRate: parseInt(formData.get("vendorRate") as string, 10),
    };
  
    if (
      !newEvent.eventName ||
      isNaN(newEvent.maxCapacity) ||
      isNaN(newEvent.customerRate) ||
      isNaN(newEvent.vendorRate)
    ) {
      alert("Please fill in all fields with valid data.");
      return;
    }
  
    try {
      const response = await fetch(`${API_BASE_URL}/event/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newEvent),
      });
  
      if (response.ok) {
        const createdEvent = await response.json();
        setEvents((prevEvents) => [...prevEvents, createdEvent]);
        setSelectedEvent(createdEvent.eventName);
        setShowModal(false); // Close the modal
        alert(`Event "${createdEvent.eventName}" created successfully!`);
      } else {
        console.error("Failed to create event");
        alert("Failed to create event.");
      }
    } catch (error) {
      console.error("Error creating event:", error);
      alert("An error occurred while creating the event.");
    }
  };
  

  const handleAddTickets = async () => {
    if (!selectedEvent) {
      alert("Please select an event first!");
      return;
    }
  
    const selectedEventData = events.find((event) => event.eventName === selectedEvent);
  
    if (!selectedEventData || !selectedEventData.id) {
      alert("Invalid event selected!");
      return;
    }
  
    const ticketCountNumber = parseInt(ticketCount, 10);
    if (isNaN(ticketCountNumber) || ticketCountNumber <= 0) {
      alert("Enter a valid ticket count.");
      return;
    }
  
    try {
      const response = await fetch(
        `${API_BASE_URL}/event/${selectedEventData.id}/tickets/add?count=${ticketCountNumber}`,
        { method: "POST" }
      );
  
      if (response.ok) {
        const message = await response.text();
        alert(message);
  
        // Update the ticketPool after adding tickets
        setEvents((prevEvents) =>
          prevEvents.map((event) =>
            event.id === selectedEventData.id
              ? { ...event, ticketPool: (event.ticketPool || 0) + ticketCountNumber }
              : event
          )
        );
        setTicketCount("");
      } else {
        const errorMessage = await response.text();
        alert(errorMessage); // Display backend error (e.g., max capacity exceeded)
      }
    } catch (error) {
      console.error("Error adding tickets:", error);
      alert("An error occurred while adding tickets.");
    }
  };
  
  const handleStartSimulation = async () => {
    if (!selectedEvent) {
      alert("Please select an event first!");
      return;
    }
  
    const selectedEventData = events.find((event) => event.eventName === selectedEvent);
  
    if (!selectedEventData || !selectedEventData.id) {
      alert("Invalid event selected!");
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/simulation/start/${selectedEventData.id}`, {
        method: "POST",
      });
  
      if (response.ok) {
        alert(`Simulation started for ${selectedEvent}!`);
      } else {
        const errorMessage = await response.text();
        alert(errorMessage);
      }
    } catch (error) {
      console.error("Error starting simulation:", error);
      alert("An error occurred while starting the simulation.");
    }
  };
  

  const handleStopSimulation = async () => {
    if (!selectedEvent) {
      alert("Please select an event first!");
      return;
    }

    const selectedEventData = events.find((event) => event.eventName === selectedEvent);

    if (!selectedEventData || !selectedEventData.id) {
      alert("Invalid event selected!");
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/simulation/stop/${selectedEventData.id}`, {
        method: "POST",
      });

      if (response.ok) {
        alert(`Simulation stopped for ${selectedEvent}!`);
      } else {
        console.error("Failed to stop simulation");
      }
    } catch (error) {
      console.error("Error stopping simulation:", error);
    }
  };

  const deleteEvent = async (eventName: string) => {
    const updatedEvents = events.filter((event) => event.eventName !== eventName);
    setEvents(updatedEvents);
    if (selectedEvent === eventName) {
      setSelectedEvent(null);
      setLogs([]);
    }
  };

  const selectedEventConfig = events.find((event) => event.eventName === selectedEvent);

  return (
    <div className="pt-20 px-6 space-y-6">
      {/* Navigation Bar */}
      <NavigationBar
        selectedEvent={selectedEvent}
        setSelectedEvent={setSelectedEvent}
        events={events.map((event) => event.eventName)}
        setShowModal={setShowModal}
      />

      {/* Ticket Sales Logs */}
      <div className="max-w-3xl mx-auto">
        <EventLogs logs={logs} />
      </div>

      {/* Event Configuration Section */}
      <div className="flex space-x-6 items-start">
        {/* Configurations */}
        {selectedEventConfig && (
          <ConfigurationCard
            eventConfig={selectedEventConfig}
            setShowModal={setShowModal}
            deleteEvent={deleteEvent}
          />
        )}

        {/* Add Tickets Section */}
        <div className="flex flex-col items-start space-y-4 w-1/3">
          <input
            type="number"
            value={ticketCount}
            onChange={(e) => setTicketCount(e.target.value)}
            placeholder="Add tickets"
            className="border rounded p-2 w-full focus:outline-blue-500 shadow"
          />
          <button
            onClick={handleAddTickets}
            className="bg-green-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-green-600 transition"
          >
            Add Tickets
          </button>
          <div className="flex space-x-4">
            <button
              onClick={handleStartSimulation}
              className="bg-green-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-green-600 transition"
            >
              Start Simulation
            </button>
            <button
              onClick={handleStopSimulation}
              className="bg-red-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-red-600 transition"
            >
              Stop Simulation
            </button>
          </div>
        </div>
      </div>

      {/* Add Event Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex justify-center items-center z-50">
          <CreateEventForm onClose={() => setShowModal(false)} onSave={handleSaveEvent} />
        </div>
      )}
    </div>
  );
};

export default Dashboard;

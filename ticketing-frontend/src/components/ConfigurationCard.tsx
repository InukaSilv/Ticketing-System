import React from "react";

interface ConfigurationCardProps {
  eventConfig: {
    eventName: string;
    vendorRate: number; 
    customerRate: number; 
    maxCapacity: number; 
    ticketPool?: number; // Add ticketPool to indicate available tickets
  };
  setShowModal: (show: boolean) => void;
  deleteEvent: (eventName: string) => void;
}

const ConfigurationCard: React.FC<ConfigurationCardProps> = ({
  eventConfig,
  setShowModal,
  deleteEvent,
}) => {
  return (
    <div className="border rounded-lg p-4 bg-gray-100 shadow w-1/3">
      <h3 className="text-lg font-semibold mb-2">Configurations</h3>
      <p>Event Name: {eventConfig.eventName}</p>
      <p>Max Capacity: {eventConfig.maxCapacity}</p>
      <p>Release Rate: {eventConfig.vendorRate}</p>
      <p>Buying Rate: {eventConfig.customerRate}</p>
      <p>Available Tickets: {eventConfig.ticketPool ?? 0}</p> {/* Show available tickets */}
      <div className="flex space-x-2 mt-4">
        <button
          className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
          onClick={() => setShowModal(true)}
        >
          Edit Config
        </button>
        <button
          className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
          onClick={() => deleteEvent(eventConfig.eventName)}
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default ConfigurationCard;

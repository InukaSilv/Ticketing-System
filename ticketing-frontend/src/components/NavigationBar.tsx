import React from "react";
import { Menu, MenuButton, MenuItem, MenuItems } from "@headlessui/react";
import { ChevronDownIcon } from "@heroicons/react/20/solid";

interface NavigationBarProps {
  selectedEvent: string | null;
  setSelectedEvent: (event: string | null) => void;
  events: string[];
  setShowModal: (show: boolean) => void;
}

const NavigationBar: React.FC<NavigationBarProps> = ({
  selectedEvent,
  setSelectedEvent,
  events,
  setShowModal,
}) => {
  return (
    <div className="w-full fixed top-0 left-0 backdrop-blur-md bg-opacity-70 shadow-lg z-10">
      <div className="max-w-screen-xl mx-auto flex justify-between items-center py-4 px-5">
        {/* Dashboard Title */}
        <h1 className="text-2xl font-bold ml-5">Dashboard</h1>

        {/* Right Section: Dropdown and Add Event Button */}
        <div className="flex items-center space-x-4 mr-5">
          {/* Dropdown Menu */}
          <Menu as="div" className="relative inline-block text-left">
            <div>
              <MenuButton className="inline-flex justify-center gap-x-1.5 rounded-md bg-gray-100 px-4 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-gray-300 hover:bg-gray-200">
                {selectedEvent || "Select Event"}
                <ChevronDownIcon className="-mr-1 h-5 w-5 text-gray-400" />
              </MenuButton>
            </div>
            <MenuItems className="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black/5 focus:outline-none">
              <div className="py-1">
                {events.length > 0 ? (
                  events.map((event, index) => (
                    <MenuItem key={index}>
                      <button
                        className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 w-full text-left"
                        onClick={() => setSelectedEvent(event)}
                      >
                        {event}
                      </button>
                    </MenuItem>
                  ))
                ) : (
                  <MenuItem>
                    <div className="block px-4 py-2 text-sm text-gray-500 text-left">
                      No events yet
                    </div>
                  </MenuItem>
                )}
              </div>
            </MenuItems>
          </Menu>

          {/* Add Event Button */}
          <button
            onClick={() => setShowModal(true)}
            className="bg-blue-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-blue-600 transition"
          >
            Add Event
          </button>
        </div>
      </div>
    </div>
  );
};

export default NavigationBar;

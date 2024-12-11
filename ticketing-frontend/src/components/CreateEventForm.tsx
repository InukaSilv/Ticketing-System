import React from "react";

type CreateEventFormProps = {
  onClose: () => void;
  onSave: (e: React.FormEvent<HTMLFormElement>) => Promise<void>;
};

const CreateEventForm: React.FC<CreateEventFormProps> = ({ onClose, onSave }) => {
  return (
    <form
      onSubmit={onSave} // Call the passed onSave function
      className="bg-white rounded-lg p-6 shadow-md w-full max-w-md"
    >
      <h2 className="text-lg font-semibold mb-4">Configure Event</h2>
      <div className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">Event Name</label>
          <input
            name="eventName"
            type="text"
            className="w-full border rounded p-2 focus:outline-blue-500"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Vendor Release Rate</label>
          <input
            name="vendorRate"
            type="number" // Changed to number
            className="w-full border rounded p-2 focus:outline-blue-500"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Customer Buying Rate</label>
          <input
            name="customerRate"
            type="number" // Changed to number
            className="w-full border rounded p-2 focus:outline-blue-500"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Max Ticket Capacity</label>
          <input
            name="maxCapacity"
            type="number" // Changed to number
            className="w-full border rounded p-2 focus:outline-blue-500"
            required
          />
        </div>
      </div>
      <div className="flex justify-end mt-4 space-x-2">
        <button
          type="button"
          onClick={onClose} // Close the modal when this button is clicked
          className="bg-gray-300 px-4 py-2 rounded hover:bg-gray-400"
        >
          Close
        </button>
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Save
        </button>
      </div>
    </form>
  );
};

export default CreateEventForm;

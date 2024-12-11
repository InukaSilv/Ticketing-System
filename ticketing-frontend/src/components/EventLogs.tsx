interface EventLogsProps {
  logs: string[];
}

const EventLogs: React.FC<EventLogsProps> = ({ logs }) => {
  return (
    <div className="border rounded-lg p-4 bg-white shadow max-h-64 overflow-y-auto">
      <h3 className="text-lg font-semibold mb-2">Ticket Sales Logs</h3>
      {logs.length > 0 ? (
        <ul className="list-disc pl-5">
          {logs.map((log, index) => (
            <li key={index}>{log}</li>
          ))}
        </ul>
      ) : (
        <p>No logs available yet.</p>
      )}
    </div>
  );
};

export default EventLogs;

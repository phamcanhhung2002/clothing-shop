import "./chart.css";
import {
  XAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  YAxis,
  BarChart,
  Bar,
  Line,
  LineChart,
} from "recharts";

export default function Chart({ title, data, dataKey, xDataKey, grid, line }) {
  return (
    <div className="chart">
      <h3 className="chartTitle">{title}</h3>
      <ResponsiveContainer width="100%" aspect={4 / 1}>
        {line ? (
          <LineChart data={data}>
            <XAxis dataKey={xDataKey} stroke="#5550bd" />
            <YAxis />
            <Line type="monotone" dataKey={dataKey} stroke="#8884d8" />
            <Tooltip />
            {grid && <CartesianGrid stroke="#e0dfdf" strokeDasharray="5 5" />}
          </LineChart>
        ) : (
          <BarChart data={data}>
            <XAxis dataKey={xDataKey} stroke="#5550bd" />
            <YAxis />
            <Bar dataKey={dataKey} type="monotone" fill="#8884d8" />
            <Tooltip />
            {grid && <CartesianGrid strokeDasharray="3 3" />}
          </BarChart>
        )}
      </ResponsiveContainer>
    </div>
  );
}

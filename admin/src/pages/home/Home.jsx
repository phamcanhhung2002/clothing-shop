import Chart from "../../components/chart/Chart";
import FeaturedInfo from "../../components/featuredInfo/FeaturedInfo";
import "./home.css";
import { userData } from "../../dummyData";
import WidgetSm from "../../components/widgetSm/WidgetSm";
import WidgetLg from "../../components/widgetLg/WidgetLg";
import { useEffect, useMemo, useState } from "react";
import { userRequest } from "../../requestMethods";
import moment from "moment";

export default function Home() {
  const date = new Date();
  const [userStats, setUserStats] = useState([]);
  const [orderData, setOrderData] = useState([]);

  const [filters, setFilters] = useState({
    fromDateFormat: moment(date).format("YYYY-MM-DD"),
    toDateFormat: moment(date).format("YYYY-MM-DD"),
    intervalType: "hour",
  });

  const timeZoneSign = date.getTimezoneOffset() < 0 ? "POSITIVE" : "NEGATIVE";
  const dateString = date.toString();
  const index = dateString.indexOf("GMT");
  const timeZoneOffset = dateString.substring(index + 4, index + 8);
  console.log("timeZoneOffset", timeZoneOffset);

  const MONTHS = useMemo(
    () => [
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "May",
      "Jun",
      "Jul",
      "Agu",
      "Sep",
      "Oct",
      "Nov",
      "Dec",
    ],
    []
  );

  // useEffect(() => {
  //   const getStats = async () => {
  //     try {
  //       const res = await userRequest.get("/users/stats");
  //       res.data.map((item) =>
  //         setUserStats((prev) => [
  //           ...prev,
  //           { name: MONTHS[item._id - 1], "Active User": item.total },
  //         ])
  //       );
  //     } catch (error) {
  //       console.log(error);
  //     }
  //   };
  //   getStats();
  // }, [MONTHS]);

  const handleChangeFilters = (e) => {
    setFilters({
      ...filters,
      [e.target.name]: e.target.value,
    });
  };

  useEffect(() => {
    getOrdersStat();
  }, []);

  const getOrdersStat = async () => {
    try {
      const { data } = await userRequest.get("/stat/orders", {
        params: {
          ...filters,
          timeZoneOffset,
          timeZoneSign,
        },
      });
      console.log(data);
      setOrderData(data);
    } catch (error) {
      console.log("error");
      console.log(error);
    }
  };

  const handleSubmitFilters = (e) => {
    getOrdersStat();
  };

  return (
    <div className="home">
      {/* <FeaturedInfo /> */}
      <div className="filterContainer">
        <span>
          <label for="fromDateFormat">From Date:</label>
          <input
            type="date"
            id="fromDateFormat"
            name="fromDateFormat"
            onChange={handleChangeFilters}
            value={filters.fromDateFormat}
          />
        </span>
        <span>
          <label for="toDateFormat">To Date:</label>
          <input
            type="date"
            id="toDateFormat"
            name="toDateFormat"
            onChange={handleChangeFilters}
            value={filters.toDateFormat}
          />
        </span>
        <span>
          <label for="intervalType">Interval:</label>
          <select
            id="intervalType"
            name="intervalType"
            onChange={handleChangeFilters}
            value={filters.intervalType}
          >
            <option>hour</option>
            <option>day</option>
            <option>month</option>
          </select>
        </span>
        <button type="submit" onClick={handleSubmitFilters}>
          Filter
        </button>
      </div>
      <Chart
        data={orderData}
        title="Order Analytics"
        grid
        xDataKey="interval"
        dataKey="quantity"
      />
      <Chart
        data={userData}
        title="User Analytics"
        grid
        xDateKey="name"
        dataKey="Active User"
        line
      />
      <div className="homeWidgets">
        {/* <WidgetSm />
        <WidgetLg /> */}
      </div>
    </div>
  );
}

import { Link, useLocation } from "react-router-dom";
import "./product.css";
import Chart from "../../components/chart/Chart";
import { Publish } from "@mui/icons-material";
import { useEffect, useMemo, useState } from "react";
import { userRequest } from "../../requestMethods";
import {
  getStorage,
  ref,
  uploadBytesResumable,
  getDownloadURL,
} from "firebase/storage";
import app from "../../firebase";
import { toast } from "react-toastify";

export default function Product() {
  const [inputs, setInputs] = useState({});
  const [file, setFile] = useState(null);
  const location = useLocation();
  const productId = parseInt(location.pathname.split("/")[2]);
  const [pStats, setPStats] = useState([]);

  const [product, setProduct] = useState({});

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

  const handleChange = (e) => {
    setInputs((prev) => {
      return { ...prev, [e.target.name]: e.target.value };
    });
  };

  useEffect(() => {
    const getProduct = async () => {
      try {
        const { data } = await userRequest.get("/products/" + productId);
        setProduct(data);
        setInputs(data);
      } catch (e) {
        console.log(e);
      }
    };

    getProduct();
  }, [productId]);

  // useEffect(() => {
  //   const getStats = async () => {
  //     try {
  //       const res = await userRequest.get("orders/income?pid=" + productId);
  //       const list = res.data.sort((a, b) => {
  //         return a.id - b.id;
  //       });
  //       list.map((item) =>
  //         setPStats((prev) => [
  //           ...prev,
  //           { name: MONTHS[item.id - 1], Sales: item.total },
  //         ])
  //       );
  //     } catch (err) {
  //       console.log(err);
  //     }
  //   };
  //   getStats();
  // }, [productId, MONTHS]);

  const handleClick = (e) => {
    e.preventDefault();
    if (file) {
      const fileName = new Date().getTime() + file.name;
      const storage = getStorage(app);
      const storageRef = ref(storage, fileName);
      const uploadTask = uploadBytesResumable(storageRef, file);

      // Register three observers:
      // 1. 'state_changed' observer, called any time the state changes
      // 2. Error observer, called on failure
      // 3. Completion observer, called on successful completion
      uploadTask.on(
        "state_changed",
        (snapshot) => {
          // Observe state change events such as progress, pause, and resume
          // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
          const progress =
            (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
          console.log("Upload is " + progress + "% done");
          switch (snapshot.state) {
            case "paused":
              console.log("Upload is paused");
              break;
            case "running":
              console.log("Upload is running");
              break;
            default:
          }
        },
        (error) => {
          // Handle unsuccessful uploads
          toast.error("Could not update product");
          console.log(error);
        },
        () => {
          // Handle successful uploads on complete
          // For instance, get the download URL: https://firebasestorage.googleapis.com/...
          getDownloadURL(uploadTask.snapshot.ref).then(updateProduct);
        }
      );
    } else {
      updateProduct(product.img);
    }
  };

  const updateProduct = async (downloadURL) => {
    try {
      const product = { ...inputs, img: downloadURL };
      console.log(product);
      await userRequest.patch("/products/" + product.id, product);
      toast.success("Update product succesfully");
    } catch (error) {
      toast.error("Could not update product");
      console.log(error);
    }
  };

  return (
    <div className="product">
      <div className="productTitleContainer">
        <h1 className="productTitle">Product</h1>
        <Link to="/newproduct">
          <button className="productAddButton">Create</button>
        </Link>
      </div>
      <div className="productTop">
        <div className="productTopLeft">
          <Chart data={pStats} dataKey="Sales" title="Sales Performance" />
        </div>
        <div className="productTopRight">
          <div className="productInfoTop">
            <img src={product.img} alt="" className="productInfoImg" />
            <span className="productName">{product.title}</span>
          </div>
          <div className="productInfoBottom">
            <div className="productInfoItem">
              <span className="productInfoKey">id:</span>
              <span className="productInfoValue">{product.id}</span>
            </div>
            <div className="productInfoItem">
              <span className="productInfoKey">sales:</span>
              <span className="productInfoValue">5123</span>
            </div>
            <div className="productInfoItem">
              <span className="productInfoKey">in stock:</span>
              <span className="productInfoValue">{product.inStock}</span>
            </div>
          </div>
        </div>
      </div>
      <div className="productBottom">
        <form className="productForm" onSubmit={handleClick}>
          <div className="productFormLeft">
            <label>Product Name</label>
            <input
              type="text"
              name="title"
              placeholder="Product Title"
              value={inputs.title || ""}
              onChange={handleChange}
            />
            <label>Product Description</label>
            <input
              type="text"
              name="description"
              placeholder="Product Description"
              onChange={handleChange}
              value={inputs.description || ""}
            />
            <label>Price</label>
            <input
              type="text"
              name="price"
              value={inputs.price || ""}
              placeholder="Product Price"
              onChange={handleChange}
            />
            <label>In Stock</label>
            <select
              name="inStock"
              id="idStock"
              placeholder="Product Price"
              onChange={handleChange}
            >
              <option value="true">Yes</option>
              <option value="false">No</option>
            </select>
          </div>
          <div className="productFormRight">
            <div className="productUpload">
              <img
                src={inputs.img || product.img}
                alt=""
                className="productUploadImg"
              />
              <label for="file">
                <Publish />
              </label>
              <input
                type="file"
                id="file"
                style={{ display: "none" }}
                onChange={(e) => {
                  setFile(e.target.files[0]);
                  setInputs({
                    ...inputs,
                    img: URL.createObjectURL(e.target.files[0]),
                  });
                }}
              />
            </div>
            <button className="productButton" onClick={handleClick}>
              Update
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

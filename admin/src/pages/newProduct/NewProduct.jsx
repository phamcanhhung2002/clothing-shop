import { useEffect, useState } from "react";
import "./newProduct.css";
import {
  getStorage,
  ref,
  uploadBytesResumable,
  getDownloadURL,
} from "firebase/storage";
import app, { folderName } from "../../firebase";
import { publicRequest, userRequest } from "../../requestMethods";
import { toast } from "react-toastify";

export default function NewProduct() {
  const [file, setFile] = useState(null);
  const [cat, setCat] = useState([]);
  const [inputs, setInputs] = useState({});

  useEffect(() => {
    const getCat = async () => {
      try {
        const { data } = await publicRequest.get("/categories");
        console.log(data);
        setCat(data);
        setInputs({ category: data[0] });
      } catch (error) {
        console.log(error);
      }
    };
    getCat();
  }, []);

  const handleChange = (e) => {
    setInputs((prev) => {
      return { ...prev, [e.target.name]: e.target.value };
    });
  };

  const handleMultiSelectChange = (e) => {
    setInputs((prev) => {
      return {
        ...prev,
        [e.target.name]: Array.from(e.target.options)
          .filter(function (option) {
            return option.selected;
          })
          .map(function (option) {
            return option.value;
          }),
      };
    });
  };

  const handleClick = (e) => {
    e.preventDefault();
    const fileName = folderName + new Date().getTime() + file.name;
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
        console.log(error);
        toast.error("Could not create product");
      },
      () => {
        // Handle successful uploads on complete
        // For instance, get the download URL: https://firebasestorage.googleapis.com/...
        getDownloadURL(uploadTask.snapshot.ref).then(async (downloadURL) => {
          const product = {
            ...inputs,
            category: {
              id: inputs?.category,
            },
            img: downloadURL,
          };
          try {
            await userRequest.post("/products", product);
            toast.success("Create product successfully");
          } catch (error) {
            console.log(error);
            toast.error("Could not create product");
          }
        });
      }
    );
  };

  return (
    <div className="newProduct">
      <h1 className="addProductTitle">New Product</h1>
      <form className="addProductForm">
        <div className="addProductItems">
          <div className="addProductLeft">
            <div className="addProductItem">
              <label>Title</label>
              <input
                name="title"
                type="text"
                placeholder="Apple Airpods"
                onChange={handleChange}
              />
            </div>
            <div className="addProductItem">
              <label>Description</label>
              <input
                name="description"
                type="text"
                placeholder="description..."
                onChange={handleChange}
              />
            </div>
            <div className="addProductItem">
              <label>Price</label>
              <input
                name="price"
                type="number"
                placeholder="100"
                onChange={handleChange}
              />
            </div>
            <div className="addProductItem">
              <label for="cat">Categories</label>
              <select name="category" id="cat" onChange={handleChange}>
                {cat.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.cat}
                  </option>
                ))}
              </select>
            </div>
            <div className="addProductItem">
              <label>Stock</label>
              <select name="inStock" onChange={handleChange}>
                <option value="true">Yes</option>
                <option value="false">No</option>
              </select>
            </div>
          </div>
          <div className="addProductMid">
            <div className="addProductItem">
              <label for="sizes">Sizes</label>
              <select
                name="sizes"
                id="sizes"
                multiple
                onChange={handleMultiSelectChange}
              >
                <option>XS</option>
                <option>S</option>
                <option>M</option>
                <option>L</option>
                <option>XL</option>
              </select>
            </div>
            <div className="addProductItem">
              <label for="colors">Colors</label>
              <select
                name="colors"
                id="colors"
                multiple
                onChange={handleMultiSelectChange}
              >
                <option>white</option>
                <option>red</option>
                <option>blue</option>
                <option>yellow</option>
                <option>green</option>
                <option>black</option>
              </select>
            </div>
          </div>
          <div className="addProductRight">
            <div className="addProductItem">
              <label>Image</label>
              <img alt="" src={inputs.img} />
              <input
                type="file"
                id="file"
                onChange={(e) => {
                  setFile(e.target.files[0]);
                  setInputs({
                    ...inputs,
                    img: URL.createObjectURL(e.target.files[0]),
                  });
                }}
              />
            </div>
          </div>
        </div>
        <button onClick={handleClick} className="addProductButton">
          Create
        </button>
      </form>
    </div>
  );
}

import "./newUser.css";
import { publicRequest } from "../../requestMethods";
import { toast } from "react-toastify";

export default function NewUser() {
  const handleCreateUser = async (e) => {
    e.preventDefault();

    try {
      const user = {};
      for (const element of Object.values(e.target.elements)) {
        user[element.name] = element.value;
      }

      const { data } = await publicRequest.post("/users", user);
      toast.success("Create user successfully");
      console.log(data);
    } catch (error) {
      toast.error("Could not create user");
      console.log(error);
    }
  };

  return (
    <div className="newUser">
      <h1 className="newUserTitle">New User</h1>
      <form className="newUserForm" action="" onSubmit={handleCreateUser}>
        <div className="newUserItem">
          <label>Username</label>
          <input type="text" placeholder="john" name="username" />
        </div>
        <div className="newUserItem">
          <label>Full Name</label>
          <input type="text" placeholder="John Smith" name="fullname" />
        </div>
        <div className="newUserItem">
          <label>Email</label>
          <input type="email" placeholder="john@gmail.com" name="email" />
        </div>
        <div className="newUserItem">
          <label>Password</label>
          <input type="password" placeholder="password" name="password" />
        </div>
        <div className="newUserItem">
          <label>Phone</label>
          <input type="text" placeholder="+1 123 456 78" name="phone" />
        </div>
        <div className="newUserItem">
          <label>Address</label>
          <input type="text" placeholder="New York | USA" name="address" />
        </div>
        <div className="newUserItem">
          <label>Gender</label>
          <div className="newUserGender">
            <input type="radio" name="gender" id="male" value="male" />
            <label for="male">Male</label>
            <input type="radio" name="gender" id="female" value="female" />
            <label for="female">Female</label>
            <input type="radio" name="gender" id="other" value="other" />
            <label for="other">Other</label>
          </div>
        </div>
        <div className="newUserItem">
          <label>Active</label>
          <select className="newUserSelect" name="active" id="active">
            <option value="yes">Yes</option>
            <option value="no">No</option>
          </select>
        </div>
        <button type="submit" className="newUserButton">
          Create
        </button>
      </form>
    </div>
  );
}

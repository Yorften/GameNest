import { useState, FormEvent, ChangeEvent } from "react";
import { Modal, Button, Label, TextInput } from "flowbite-react";
import { useAppDispatch } from "../app/hooks";
import { login } from "../features/auth/authSlice";


interface LoginModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export function LoginModal({ isOpen, onClose }: LoginModalProps) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [usernameError, setUsernameError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [serverError, setServerError] = useState("");

  const dispatch = useAppDispatch();

  const handleUsernameChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setUsername(value);

    if (!value.trim()) {
      setUsernameError("Username is required.");
    } else {
      setUsernameError("");
    }
  };

  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setPassword(value);

    if (!value.trim()) {
      setPasswordError("Password is required.");
    } else {
      setPasswordError("");
    }
  };


  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    let isValid = true;

    if (!username.trim()) {
      setUsernameError("Username is required.");
      isValid = false;
    }
    if (!password.trim()) {
      setPasswordError("Password is required.");
      isValid = false;
    }

    if (!isValid) return;

    try {
      await dispatch(login({ username, password })).unwrap();
      setServerError("");
      setUsername("");
      setPassword("");
      onClose();
    } catch (err: any) {
      setServerError(err);
    }
  };

  return (
    <Modal show={isOpen} onClose={onClose}>
      <Modal.Header>Login</Modal.Header>
      <Modal.Body>
        <form onSubmit={handleSubmit} className="space-y-6">
          {serverError && <p className="text-red-600 text-sm">{serverError}</p>}
          <div>
            <Label htmlFor="username" value="Username" />
            <TextInput
              id="username"
              placeholder="username"
              required
              value={username}
              onChange={handleUsernameChange}
              color={usernameError ? "failure" : ""}
              helperText={usernameError && <span className="text-red-600">{usernameError}</span>}
            />
          </div>

          <div>
            <Label htmlFor="password" value="Password" />
            <TextInput
              id="password"
              type="password"
              placeholder="••••••••"
              required
              value={password}
              onChange={handlePasswordChange}
              color={passwordError ? "failure" : ""}
              helperText={passwordError && <span className="text-red-600">{passwordError}</span>}
            />
          </div>
        </form>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={handleSubmit}>Login</Button>
        <Button color="gray" onClick={onClose}>
          Cancel
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

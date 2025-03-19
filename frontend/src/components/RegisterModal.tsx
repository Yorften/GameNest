import { useState, FormEvent, ChangeEvent } from "react";
import { Modal, Button, Label, TextInput, Select } from "flowbite-react";
import { useAppDispatch } from "../app/hooks";
// import { useAppDispatch } from "../../store/hooks";
// import { registerUser } from "../../store/authSlice"; // hypothetical register action

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

interface RegisterModalProps {
    isOpen: boolean;
    onClose: () => void;
}

export function RegisterModal({ isOpen, onClose }: RegisterModalProps) {
    // Local states
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");

    const [usernameError, setUsernameError] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [repeatPasswordError, setRepeatPasswordError] = useState("");

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

    const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setEmail(value);

        if (!value.trim()) {
            setEmailError("Email is required.");
        } else if (!emailRegex.test(value)) {
            setEmailError("Please enter a valid email address.");
        } else {
            setEmailError("");
        }
    };

    const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setPassword(value);

        if (!value.trim()) {
            setPasswordError("Password is required.");
        } else if (value.trim().length < 6) {
            setPasswordError("Password must be at least 6 characters long.");
        } else {
            setPasswordError("");
        }
    };

    const handleRepeatPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setRepeatPassword(value);

        if (!value.trim()) {
            setRepeatPasswordError("Please confirm your password.");
        } else if (value !== password) {
            setRepeatPasswordError("Passwords do not match.");
        } else {
            setRepeatPasswordError("");
        }
    };

    // Final check on submit
    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();

        let isValid = true;

        // Ensure no fields are empty
        if (!username.trim()) {
            setUsernameError("Username is required.");
            isValid = false;
        }
        if (!email.trim()) {
            setEmailError("Email is required.");
            isValid = false;
        } else if (!emailRegex.test(email)) {
            setEmailError("Please enter a valid email address.");
            isValid = false;
        }
        if (!password.trim()) {
            setPasswordError("Password is required.");
            isValid = false;
        } else if (password.trim().length < 6) {
            setPasswordError("Password must be at least 6 characters long.");
            isValid = false;
        }
        if (!repeatPassword.trim()) {
            setRepeatPasswordError("Please confirm your password.");
            isValid = false;
        } else if (repeatPassword !== password) {
            setRepeatPasswordError("Passwords do not match.");
            isValid = false;
        }

        if (!isValid) return;

        // Build the final registration payload
        const payload = {
            username,
            password,
            repeatPassword,
            email,
            role: { name: "ROLE_USER" }, // default role
        };

        // Usually you'd dispatch your registration action:
        // dispatch(registerUser(payload))
        //   .unwrap()
        //   .then(() => onClose())
        //   .catch((err) => /* handle error */);

        console.log("Register payload:", payload);
        onClose();
    };

    return (
        <Modal show={isOpen} onClose={onClose}>
            <Modal.Header>Register</Modal.Header>
            <Modal.Body>
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <Label htmlFor="username" value="Username" />
                        <TextInput
                            id="username"
                            placeholder="Your username"
                            required
                            value={username}
                            onChange={handleUsernameChange}
                            color={usernameError ? "failure" : ""}
                            helperText={usernameError && <span className="text-red-600">{usernameError}</span>}
                        />
                    </div>

                    <div>
                        <Label htmlFor="email" value="Email" />
                        <TextInput
                            id="email"
                            type="email"
                            placeholder="newuser@example.com"
                            required
                            value={email}
                            onChange={handleEmailChange}
                            color={emailError ? "failure" : ""}
                            helperText={emailError && <span className="text-red-600">{emailError}</span>}
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

                    <div>
                        <Label htmlFor="repeatPassword" value="Confirm Password" />
                        <TextInput
                            id="repeatPassword"
                            type="password"
                            placeholder="••••••••"
                            required
                            value={repeatPassword}
                            onChange={handleRepeatPasswordChange}
                            color={repeatPasswordError ? "failure" : ""}
                            helperText={repeatPasswordError && (
                                <span className="text-red-600">{repeatPasswordError}</span>
                            )}
                        />
                    </div>
                </form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={handleSubmit}>Register</Button>
                <Button color="gray" onClick={onClose}>
                    Cancel
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

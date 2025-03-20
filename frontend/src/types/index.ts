import React, { Dispatch, MouseEventHandler, ReactNode, SetStateAction } from "react";

export interface ButtonProps {
  title: ReactNode;
  className?: string;
  type?: "submit" | "button" | "reset";
  onClick?: MouseEventHandler<HTMLButtonElement>;
  id?: string;
  disabled?: boolean;
}

export interface User {
  username: string;
  email: string;
  installationId: number;
  role: { name: string };
}

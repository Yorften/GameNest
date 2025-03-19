import { ButtonProps } from "../../types";

export default function Button({ id, type, className, onClick, title, disabled }: ButtonProps) {
  return (
    <button
      className={`flex py-2 px-8 text-lg rounded-md text-white bg-primary shadow-md hover:shadow-lg hover:bg-secondary transition-all duration-200 ${className}`}
      disabled={disabled}
      type={type}
      onClick={onClick}
      id={id}
    >
      <span className={`z-20 flex-1 pt-[2px]`}>
        <center>{title}</center>
      </span>
    </button>
  );
}

import * as React from "react";

type ButtonProps = {
  cancel?: boolean
  label: string
  onClick: any
}

export const Button = ({label, onClick, cancel = false}: ButtonProps) => (
  <button
    className={`p-2 rounded-md uppercase text-sm font-medium min-w-8 ${cancel ? 'bg-background text-black' : 'bg-primary-100 text-white'}`}
    onClick={onClick}>{label}</button>
);

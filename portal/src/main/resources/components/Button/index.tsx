import * as React from "react";

type ButtonProps = {
  cancel?: boolean
  label: string
  onClick: any
  disabled?: boolean
}

export const Button = ({label, onClick, cancel = false, disabled = false}: ButtonProps) => (
  <button
    className={`p-2 rounded-md uppercase text-sm font-medium min-w-4 ${cancel ? 'bg-background text-black' : 'bg-primary-100 text-white'} ${disabled ? 'cursor-not-allowed bg-grey-100' : ''}`}
    onClick={onClick} disabled={disabled}>{label}</button>
);

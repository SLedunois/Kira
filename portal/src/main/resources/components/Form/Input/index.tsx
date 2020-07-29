import React, {ChangeEventHandler, KeyboardEventHandler} from 'react';

import {UUID} from "../../../utils";

export type IInput = {
  label: string
  value: any
  onChange: ChangeEventHandler<HTMLInputElement>
  onKeyDown?: KeyboardEventHandler<HTMLInputElement>
  onKeyUp?: KeyboardEventHandler<HTMLInputElement>
  disabled?: boolean
}

export const Input = ({label, value, onChange, disabled = false, onKeyDown, onKeyUp}: IInput) => {
  const id = UUID();

  return (
    <p>
      <label htmlFor={id} className="text-base font-regular color-black">{label}</label>
      <input type="text" id={id}
             className="w-full mt-2 h-10 p-2 rounded-md border-solid border border-border focus:border-primary-100 outline-none transition ease-in-out duration-200"
             disabled={disabled} value={value} onChange={onChange} onKeyDown={onKeyDown} onKeyUp={onKeyUp}/>
    </p>
  );
}

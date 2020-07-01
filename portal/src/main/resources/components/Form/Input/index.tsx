import React, {ChangeEventHandler, KeyboardEventHandler} from 'react';

type IInput = {
  label: string
  value: any
  onChange: ChangeEventHandler<HTMLInputElement>
  onKeyDown?: KeyboardEventHandler<HTMLInputElement>
  onKeyUp?: KeyboardEventHandler<HTMLInputElement>
  disabled?: boolean
}

export const Input = ({label, value, onChange, disabled = false, onKeyDown, onKeyUp}: IInput) => (
  <p>
    <label htmlFor="project-name" className="text-base font-regular color-black">{label}</label>
    <input type="text" id="project-name"
           className="w-full mt-2 h-10 p-2 rounded-sm border-solid border border-border font-semibold"
           disabled={disabled} value={value} onChange={onChange} onKeyDown={onKeyDown} onKeyUp={onKeyUp}/>
  </p>
);

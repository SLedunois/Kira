import React, {ChangeEventHandler} from 'react';

import {UUID} from "../../../utils";

export type ITextarea = {
  label: string
  value: any
  onChange: ChangeEventHandler<HTMLTextAreaElement>
  disabled?: boolean
  rows?: number
}

export const Textarea = ({label, disabled, value, onChange, rows = 3}: ITextarea) => {
  const id = UUID();

  return (
    <p>
      <label htmlFor={id} className="text-base font-regular color-black">{label}</label>
      <textarea id={id}
                className="w-full mt-2 p-2 rounded-md border-solid border border-border focus:border-primary-100 outline-none transition ease-in-out duration-200"
                disabled={disabled} rows={rows} value={value} onChange={onChange}/>
    </p>
  )
}

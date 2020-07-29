import React, {ReactElement, useState} from 'react';
import {ClickOutsideHandler} from "../../ClickOutsideHandler";
import {UUID} from "../../../utils";
import {Caret} from "@ui/icons";

type ISelect = {
  label: string
  value: ReactElement
  children: ReactElement[]
}

export const Select = ({value, children, label}: ISelect) => {
  const id = UUID();
  const [displayed, display] = useState(false);

  return (
    <div className="relative">
      <label htmlFor={id} className="text-base font-regular color-black">{label}</label>
      <ClickOutsideHandler onClickOutside={() => display(false)}>
        <div
          className="flex justify-between items-center flex-row bg-white w-full mt-2 h-10 py-2 rounded-md border-solid border border-border  hover:border-primary-100 outline-none transition ease-in-out duration-200 cursor-pointer"
          id={id} onClick={() => display(!displayed)}>
          {value}
          <Caret className="fill-black w-3 h-3 ml-4 mr-2"/>
        </div>
        {
          displayed && <div
            className="absolute w-full bg-white rounded-lg shadow-xl min-w-10 flex flex-col overflow-auto max-h-10">
            {children.map(child => <div key={child.key} onClick={() => display(false)}
                                        className="bg-white hover:bg-background transition ease-in-out duration-200 cursor-pointer">{child}</div>)}
          </div>
        }
      </ClickOutsideHandler>
    </div>
  )
}

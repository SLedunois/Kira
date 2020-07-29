import React, {useState} from 'react';
import {Caret} from "@ui/icons";
import {ClickOutsideHandler} from "@ui/ClickOutsideHandler";

type ISelect = {
  selected: any
  options: any[]
  onChange: any
}

export const Select = ({selected, options, onChange}: ISelect) => {
  const [show, display] = useState(false);

  const onClick = (option: any) => {
    onChange(option);
    display(false);
  }

  return (
    <ClickOutsideHandler onClickOutside={() => display(false)}>
      <div className="relative">
        <div
          className="flex flex-row justify-center items-center m-4 cursor-pointer p-2"
          onClick={() => display(!show)}>
          {selected.name}
          <Caret className="fill-black w-3 h-3 ml-4"/></div>
        {
          show &&
          <ul
            className="absolute w-full bg-white rounded-lg shadow-xl min-w-10 flex flex-col justify-center overflow-hidden">
            {
              options.map(option => <li
                className="p-2 bg-white hover:bg-primary-100 hover:text-white transition ease-in-out duration-300 cursor-pointer"
                key={option.id}
                onClick={() => onClick(option)}>{option.name}</li>)
            }
          </ul>
        }
      </div>
    </ClickOutsideHandler>
  )
}

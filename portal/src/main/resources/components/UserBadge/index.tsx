import * as React from "react";
import {initials} from "../../utils";
import {Close} from "@ui/icons";

type IUserBadge = {
  label: string
  color: string
  size?: string
  border?: boolean
}

type IUserSelection = {
  lastName: string
  firstName: string
  color: string
  onRemove: any
}

export const UserBadge = ({label, color, size = "big", border = false}: IUserBadge) => {
  const wh = size === "big" ? 'w-10 h-10' : 'w-8 h-8';
  const fSize = size === "big" ? 'text-xl' : 'text-l font-bold';
  const borderStyle = border ? 'border-white border-2' : ''
  return (
    <div
      className={`bg-${color}-100 text-white ${wh} ${fSize} ${borderStyle} flex items-center justify-center rounded-full box-content`}>
      {label}
    </div>
  );
}

export const UserSelection = ({firstName, lastName, color, onRemove}: IUserSelection) => (
  <div className="bg-white pr-1 py-1 inline-block flex flex-row items-center justify-center mr-2 rounded-lg mb-2">
    <div className="ml-3 mr-2">
      <UserBadge size={"small"} color={color} label={initials(firstName, lastName)}/>
    </div>
    <div>
      {firstName} {lastName}
    </div>
    <div className="mx-2">
      <Close className={"fill-black w-3 cursor-pointer"} onClick={onRemove}/>
    </div>
  </div>
)

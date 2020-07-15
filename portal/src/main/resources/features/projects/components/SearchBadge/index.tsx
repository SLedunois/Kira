import React from 'react';

import {UserBadge} from "@ui/UserBadge";
import {Close} from "@ui/icons";

import {initials} from "../../../../utils";

type ISearchBadge = {
  lastName: string
  firstName: string
  color: string
  onRemove: any
}

export const SearchBadge = ({firstName, lastName, color, onRemove}: ISearchBadge) => (
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

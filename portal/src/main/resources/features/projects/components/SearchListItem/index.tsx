import React from 'react';

import {UserBadge} from "@ui/UserBadge";
import {initials} from "../../../../utils";

type ISearchListItem = {
  lastName: string
  firstName: string
  color: string
  onClick: any
}

export const SearchListItem = ({lastName, firstName, color, onClick}: ISearchListItem) => (
  <div className="p-2 flex flex-row items-center" onClick={onClick}>
    <UserBadge label={initials(lastName, firstName)} color={color} size="small"/>
    <div>
      {firstName} {lastName}
    </div>
  </div>
);

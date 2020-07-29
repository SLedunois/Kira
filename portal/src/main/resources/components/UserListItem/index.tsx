import React from 'react';

import {UserBadge} from "../UserBadge";
import {initials} from "../../utils";

type IUserListItem = {
  lastName: string
  firstName: string
  color: string
  onClick: any
}

export const UserListItem = ({lastName, firstName, color, onClick}: IUserListItem) => (
  <div className="p-2 flex flex-row items-center" onClick={onClick}>
    <UserBadge label={initials(lastName, firstName)} color={color} size="small"/>
    <div className="ml-2">
      {firstName} {lastName}
    </div>
  </div>
);

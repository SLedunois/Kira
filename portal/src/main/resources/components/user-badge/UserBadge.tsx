import * as React from "react";

interface IUserBadge {
  label: string,
  color: string
}

export const UserBadge = (props: IUserBadge) => (
  <div
    className={`bg-${props.color}-25 text-${props.color}-100 w-10 h-10 text-xl flex items-center justify-center ml-6 mr-4 rounded-full rounded-tl-lg`}>
    {props.label}
  </div>
);

import * as React from "react";

type IUserBadge = {
  label: string
  color: string
  size?: string
}

export const UserBadge = ({label, color, size = "big"}: IUserBadge) => {
  const wh = size === "big" ? 'w-10 h-10' : 'w-8 h-8';
  const fSize = size === "big" ? 'text-xl' : 'text-l font-bold';
  return (
    <div
      className={`bg-${color}-25 text-${color}-100 ${wh} ${fSize} flex items-center justify-center rounded-full rounded-tl-lg`}>
      {label}
    </div>
  );
}

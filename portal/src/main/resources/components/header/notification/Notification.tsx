import * as React from "react";

import {Notification as NotificationIcon} from '@ui/icons';

import './notification.css';

export const Notification = () => (
  <div className="border-l-2 border-r-2 border-border h-full flex items-center">
    <div className="ml-6 mr-6 relative">
      <NotificationIcon className="fill-black w-7"/>
      <div
        className="absolute notification-badge bg-secondary-100 w-4 h-4 text-xs flex items-center justify-center text-white rounded-full font-medium">
        6
      </div>
    </div>
  </div>
);

import * as React from "react";

import {Notification} from './notification/Notification'
import {Profile} from "./profile/Profile";

import Logo from '@assets/img/logo.svg';

interface IHeader {
  user: any
}

export const Header = (props: IHeader) => (
  <div className="bg-white h-16 border-b-2 border-border flex items-center justify-between fixed w-full top-0 left-0">
    <img src={Logo} alt="Kyra" className="ml-6"/>
    <div className="h-full flex">
      <Notification/>
      <Profile firstName={props.user.first_name} lastName={props.user.last_name}/>
    </div>
  </div>
);

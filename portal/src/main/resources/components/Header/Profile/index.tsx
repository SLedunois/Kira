import * as React from "react";

import {UserBadge} from '@ui/UserBadge';
import {Chevron, Logout} from '@ui/icons';
import {initials} from "../../../utils";

import './profile.css';
import {useTranslation} from "react-i18next";

interface IProfile {
  firstName: string
  lastName: string
}

export const Profile = (props: IProfile) => {
  const {t} = useTranslation();
  const firstName = props.firstName || '';
  const lastName = props.lastName || '';

  const [opened, setOpened] = React.useState(false);

  function _open() {
    setOpened(!opened)
  }

  return (
    <div className="h-full" onClick={_open}>
      <div className="flex items-center cursor-pointer h-full">
        <div className="ml-6 mr-4">
          <UserBadge label={initials(firstName, lastName)} color="primary"/>
        </div>
        <div className="mr-4 font-medium text-base">{firstName} {lastName}</div>
        <Chevron className="mr-6 fill-black w-3"/>
      </div>
      {
        opened &&
        <div className="profile-options bg-white shadow-xl mt-2 mr-1 rounded-md inline-block float-right pr-4 pl-4">
          <ul>
            <li className="h-12 flex items-center p-1 pr-3 text-right">
              <a href="/account/sign-out" className="w-full flex justify-end">
                <Logout className="fill-black mr-2"/>
                {t('sign out')}
              </a>
            </li>
          </ul>
        </div>
      }
    </div>
  );
}

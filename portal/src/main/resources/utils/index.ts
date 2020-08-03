import {Member} from "../features/projects/types";
import http from "axios";

export function initials(firstName: string, lastName: string): string {
  return `${firstName[0]}${lastName[0]}`.toUpperCase();
}

export function prepareMembers(members: Member[]) {
  const arr = members.length > 5 ? members.slice(0, 5) : members;
  if (members.length > 5) arr.push({
    email: null,
    first_name: '+',
    last_name: (members.length - arr.length).toString(),
    color: 'grey'
  });

  return arr;
}

export function UUID() {
  let dt = new Date().getTime();
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    let r = (dt + Math.random() * 16) % 16 | 0;
    dt = Math.floor(dt / 16);
    return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
  });
}

export async function searchMembers(value: string) {
  const {data} = await http.get(`/account/search?q=${value}`);
  return data;
}

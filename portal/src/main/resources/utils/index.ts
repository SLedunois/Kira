import {Member} from "../features/projects/types";

export function randomColor(): string {
  const colors = ['primary', 'secondary', 'purple', 'yellow'];

  return colors[Math.floor(Math.random() * colors.length)];
}

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

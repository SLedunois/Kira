export function randomColor(): string {
  const colors = ['primary', 'secondary', 'purple', 'yellow'];

  return colors[Math.floor(Math.random() * colors.length)];
}

export function initials(firstName: string, lastName: string): string {
  return `${firstName[0]}${lastName[0]}`.toUpperCase();
}

import * as React from 'react';

interface IChevron {
  className?: string
}

export const Chevron = (props: IChevron) => (
  <svg viewBox="0 0 10 6" fill="none" xmlns="http://www.w3.org/2000/svg" {...props}>
    <path
      d="M2.41421 0.5C1.52331 0.5 1.07714 1.57714 1.70711 2.20711L4.29289 4.79289C4.68342 5.18342 5.31658 5.18342 5.70711 4.79289L8.2929 2.20711C8.92286 1.57714 8.47669 0.5 7.58579 0.5H2.41421Z"/>
  </svg>
);

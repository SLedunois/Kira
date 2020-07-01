import React from 'react';

import LoaderIcon from '../../assets/img/loader.svg';

export const Loader = () => (
  <div className="flex items-center justify-center min-h-content">
    <img src={LoaderIcon}/>
  </div>
);

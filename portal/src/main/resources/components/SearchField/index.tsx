import React, {ChangeEvent, useEffect, useState} from 'react';

import {Input} from '@ui/Form/Input/index';

type ISearchField = {
  label: string
  onChange: any
  value: string
  children?: any
}

export const SearchField = (props: ISearchField) => {
  let typingTimeout: any;
  const [loading, setLoading] = useState(false);
  const [value, setValue] = useState(props.value);

  useEffect(() => {
    setValue(props.value);
  }, [props]);

  const onKeyDown = () => {
    clearTimeout(typingTimeout);
  }

  const onKeyUp = () => {
    clearTimeout(typingTimeout);
    typingTimeout = setTimeout(() => {
      setLoading(value.trim() !== '');
      props.onChange(value);
    }, 400);
  }

  const onInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    setValue(event.target.value);
  }

  return (
    <div className="relative">
      <Input label={props.label} value={value} onKeyDown={onKeyDown} onKeyUp={onKeyUp}
             onChange={onInputChange}/>
      <div className="flex flex-col shadow-lg rounded-lg bg-white absolute w-full">
        {props.children}
      </div>
    </div>
  );
}

import React, {useEffect, useState} from 'react';
import {useTranslation} from "react-i18next";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store";

import {cancelTicketCreation, createTicket} from '../../KanbanSlice';

import {Modal} from "@ui/Modal";
import {Input, Textarea} from "@ui/Form";
import {Select} from "@ui/Form/Select";
import {UserListItem} from "@ui/UserListItem";

export const TicketModal = () => {
  const dispatch = useDispatch();
  const {t} = useTranslation();
  const {ticket, activityOrder} = useSelector((state: RootState) => state.kanbanReducer);
  const {user, project} = useSelector((state: RootState) => state.appReducer);
  const [content, setContent] = useState('');
  const [name, setName] = useState('');
  const [assignee, setAssignee] = useState(user);
  const [enabled, enable] = useState(true);

  useEffect(() => {
    if (ticket) {
      setContent(ticket.content);
      setName(ticket.name);
      setAssignee(ticket.assignee || user);
      checkStatus();
    }
  }, [ticket]);

  const checkStatus = () => {
    enable(content.trim() !== '' && name.trim() !== '');
  }

  const ticketCreationValidation = () => {
    let ticket = {
      kanbanId: project.id,
      name,
      content,
      assignee: assignee.email,
      activity_id: activityOrder[0]
    }
    dispatch(createTicket(ticket));
  }

  if (!project || !user || !assignee) return null;


  return (
    <Modal active={ticket !== null} onClose={() => dispatch(cancelTicketCreation())}
           onValidation={ticketCreationValidation}
           title={t('add a ticket')}
           disabled={!enabled}>
      <div className="mb-4">
        <Input label={t('summary')} value={name} onChange={(evt) => {
          setName(evt.target.value);
          checkStatus()
        }}/>
      </div>
      <div className="mb-4">
        <Select
          value={<UserListItem lastName={assignee.last_name} firstName={assignee.first_name} color={assignee.color}
                               onClick={null}/>} label={t('assignee')}>
          {
            project
              .members
              .filter(member => member.email !== assignee.email)
              .map(member => <UserListItem key={member.email} firstName={member.first_name}
                                           lastName={member.last_name}
                                           onClick={() => {
                                             setAssignee(member);
                                             checkStatus()
                                           }} color={member.color}/>)
          }
        </Select>
      </div>
      <Textarea label={t('description')} value={content} onChange={(evt) => {
        setContent(evt.target.value);
        checkStatus()
      }}/>
    </Modal>
  );
};

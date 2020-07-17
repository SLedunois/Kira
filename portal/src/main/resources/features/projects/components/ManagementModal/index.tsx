import React, {useEffect, useState} from 'react';
import {Input} from "@ui/Form/Input";
import {SearchField} from "@ui/SearchField";
import {SearchListItem} from "../SearchListItem";
import {SearchBadge} from "../SearchBadge";
import {Modal} from "@ui/Modal";
import {Member, Project} from "../../types";
import http from "axios";
import {randomColor} from "../../../../utils";
import {useTranslation} from "react-i18next";

type IProjectModal = {
  project: Project
  show: boolean
  onClose: any
  onValidation: any
}

export const ProjectModal = ({project, show, onClose, onValidation}: IProjectModal) => {
  const {t} = useTranslation();
  const [projectName, setProjectName] = useState(project.name);
  const [memberName, setMemberName] = useState('');
  const [members, setMembers] = useState([]);
  const [projectMembers, setProjectMembers] = useState(project.members);

  useEffect(() => {
    setProjectName(project.name);
    setProjectMembers(project.members);
  }, [project]);

  function resetForm() {
    setProjectMembers([]);
    setMembers([]);
    setMemberName('');
    setProjectName('');
  }

  const searchForMembers = async (value: string) => {
    try {
      if (value.trim() === '') {
        resetForm();
        return;
      }

      const {data} = await http.get(`/account/search?q=${value}`);
      (data as Member[]).map(member => member.color = randomColor());
      setMembers(data);
      setMemberName(value);
    } catch (e) {
      throw e;
    }
  }

  const addProjectMember = (member: Member) => {
    const found = projectMembers.find(el => el.email === member.email);
    if (!found) {
      setProjectMembers([...projectMembers, member]);
      setMembers([]);
      setMemberName('');
    }
  }

  const onModalValidation = () => {
    onValidation({...project, name: projectName, members: projectMembers});
    resetForm();
  }

  const onModalClose = () => {
    onClose();
    resetForm();
  }

  const removeProjectMember = (member: Member) => {
    setProjectMembers(projectMembers.filter(pm => pm.email !== member.email))
  }

  return (
    <Modal title={project.id ? t('edit project') : t('add project')}
           validationLabel={project.id ? t('update') : t('add')}
           active={show}
           onClose={onModalClose}
           onValidation={onModalValidation}>
      <div className="mb-6">
        <Input label={t('project name')} value={projectName} onChange={(event) => setProjectName(event.target.value)}/>
      </div>
      <div className="mb-4">
        <SearchField label={t('invite members')} value={memberName} onChange={searchForMembers}>
          {

            members.map(member => <SearchListItem key={member.email} lastName={member.last_name}
                                                  firstName={member.first_name}
                                                  color={member.color}
                                                  onClick={() => addProjectMember(member)}/>)
          }
        </SearchField>
      </div>
      <div className="flex flex-row flex-wrap">
        {
          projectMembers.map(member => <SearchBadge key={member.email} lastName={member.last_name}
                                                    firstName={member.first_name}
                                                    color={member.color} onRemove={() => removeProjectMember(member)}/>)
        }
      </div>
    </Modal>
  )
}

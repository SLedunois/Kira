import React, {useEffect, useState} from "react";
import http from 'axios';
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";

import {Button} from "@ui/Button";
import {Modal} from '@ui/Modal';
import {Input} from '@ui/Form';
import {Loader} from "@ui/Loader";
import {SearchField} from "@ui/SearchField";
import EmptyScreen from '@assets/img/onboarding/empty_projects.svg';

import {SearchListItem} from "./components/SearchListItem";
import {createProjects, fetchProjects} from './ProjectSlice';
import {ProjectsList} from "./components/ProjectsList";
import {Member} from "./types";
import {SearchBadge} from "./components/SearchBadge";
import {randomColor} from "../../utils";

export const Projects = () => {
  const dispatch = useDispatch();
  const {projects, loading} = useSelector((state: RootState) => state.projectReducer);
  const [sort, setSort] = useState(1);
  const [displayModal, showModal] = useState(false);
  const [projectName, setProjectName] = useState('');
  const [memberName, setMemberName] = useState('');
  const [members, setMembers] = useState([]);
  const [projectMembers, setProjectMembers] = useState([]);

  useEffect(() => {
    dispatch(fetchProjects(sort));
  }, [dispatch]);

  const resetCreationForm = () => {
    showModal(false);
    setProjectName('');
    setMemberName('');
    setMembers([]);
    setProjectMembers([]);
  }

  const modalValidation = async () => {
    await dispatch(createProjects({name: projectName, members: projectMembers}));
    dispatch(fetchProjects(sort))
    resetCreationForm();
  }

  const sortProjects = () => {
    setSort(sort * -1);
    dispatch(fetchProjects(sort))
  }

  const searchForMembers = async (value: string) => {
    try {
      if (value.trim() === '') {
        setMembers([]);
        setMemberName('');
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

  const nonEmptyProjects = (
    <div className="min-h-content">
      <div className="flex py-10 justify-end items-center">
        <Button label="Add a project" onClick={() => {
          showModal(true);
        }}/>
      </div>
      <div>
        <div>
          <Button label={"Sort"} onClick={sortProjects} cancel={true}/>
        </div>
        <ProjectsList projects={projects}/>
      </div>
    </div>
  );

  const emptyProjects = (
    <div className="flex justify-center items-center min-h-content flex-col">
      <img src={EmptyScreen} className="w-1/4"/>
      <div className="mt-10 font-medium text-4xl">No projects found ?</div>
      <div className="mt-5 text-2xl text-center">
        <button className="text-primary-100" onClick={() => {
          showModal(true);
        }}>
          Create a new project
        </button>
        &nbsp;
        from scratch <br/>and assign tasks
      </div>
    </div>
  );

  const renderContent = () => {
    if (loading) return <Loader/>
    if (projects.length > 0) return nonEmptyProjects;
    else return emptyProjects;
  }

  const addProjectMember = (member: Member) => {
    const found = projectMembers.find(el => el.email === member.email);
    if (!found) {
      setProjectMembers([...projectMembers, member]);
      setMembers([]);
      setMemberName('');
    }
  }

  return (
    <div className="px-16 min-h-content">
      {
        renderContent()
      }
      <Modal title="Add new project" active={displayModal} onClose={resetCreationForm}
             onValidation={() => modalValidation()}>
        <div className="mb-6">
          <Input label={"Project name"} value={projectName} onChange={(event) => setProjectName(event.target.value)}/>
        </div>
        <div className="mb-4">
          <SearchField label={"Invite members"} value={memberName} onChange={searchForMembers}>
            {

              members.map(member => <SearchListItem key={member.email} lastName={member.last_name}
                                                    firstName={member.first_name}
                                                    color={member.color}
                                                    onClick={() => addProjectMember(member)}/>)
            }
          </SearchField>
        </div>
        <div className="flex flex-row">
          {
            projectMembers.map(member => <SearchBadge key={member.email} lastName={member.last_name}
                                                      firstName={member.first_name}
                                                      color={member.color} onRemove={() => alert('onRemove')}/>)
          }
        </div>
      </Modal>
    </div>
  );
}

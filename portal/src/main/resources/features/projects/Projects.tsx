import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";

import {Button} from "@ui/Button";
import {Loader} from "@ui/Loader";
import EmptyScreen from '@assets/img/onboarding/empty_projects.svg';

import {ProjectModal} from './components/ManagementModal';
import {createProjects, deleteProject, fetchProjects, updateProject} from './ProjectSlice';
import {ProjectsList} from "./components/ProjectsList";
import {Project} from "./types";
import {DeletionModal} from "./components/DeletionModal";
import {Trans, useTranslation} from "react-i18next";

const EMPTY_PROJECT: Project = {
  name: '',
  members: []
}

export const Projects = () => {
  const {t} = useTranslation();
  const dispatch = useDispatch();
  const {projects, loading} = useSelector((state: RootState) => state.projectReducer);
  const [project, setProject] = useState(EMPTY_PROJECT);
  const [sort, setSort] = useState(1);
  const [displayModal, showModal] = useState(false);
  const [displayDeletion, showDeletion] = useState(false);

  useEffect(() => {
    dispatch(fetchProjects(sort));
  }, [dispatch]);

  const resetCreationForm = () => {
    showModal(false);
    setProject(EMPTY_PROJECT);
  }

  const modalValidation = async (project: Project) => {
    if (project.id) {
      await dispatch(updateProject(project));
    } else {
      await dispatch(createProjects(project));
    }

    dispatch(fetchProjects(sort));
    resetCreationForm();
  }

  const deletionValidation = async (project: Project) => {
    await dispatch(deleteProject(project));
    dispatch(fetchProjects(sort));
    showDeletion(false);
    setProject(EMPTY_PROJECT);
  };

  const sortProjects = () => {
    setSort(sort * -1);
    dispatch(fetchProjects(sort))
  }

  const edit = (project: Project) => {
    setProject(project);
    showModal(true);
  }

  const drop = (project: Project) => {
    setProject(project);
    showDeletion(true);
  }

  const onDeletionClose = () => {
    showDeletion(false);
    setProject(EMPTY_PROJECT);
  }

  const nonEmptyProjects = (
    <div className="min-h-content">
      <div className="flex py-10 justify-end items-center">
        <Button label={t('add a project')} onClick={() => {
          showModal(true);
        }}/>
      </div>
      <div>
        <div>
          <Button label={t('sort')} onClick={sortProjects} cancel={true}/>
        </div>
        <ProjectsList projects={projects} edit={edit} drop={drop}/>
      </div>
    </div>
  );

  const emptyProjects = (
    <div className="flex justify-center items-center min-h-content flex-col">
      <img src={EmptyScreen} className="w-1/4"/>
      <div className="mt-10 font-medium text-4xl">{t('no projects found')} ?</div>
      <div className="mt-5 text-2xl text-center">
        <button className="text-primary-100" onClick={() => {
          showModal(true);
        }}>
          {t('create a new project')}
        </button>
        &nbsp; <Trans t={t}>from scratch and assign tasks</Trans>
      </div>
    </div>
  );

  const renderContent = () => {
    if (loading) return <Loader/>
    if (projects.length > 0) return nonEmptyProjects;
    else return emptyProjects;
  }

  return (
    <div className="px-16 min-h-content">
      {renderContent()}
      <ProjectModal project={project} show={displayModal} onClose={resetCreationForm} onValidation={modalValidation}/>
      <DeletionModal project={project} show={displayDeletion} onClose={onDeletionClose}
                     onValidation={deletionValidation}/>
    </div>
  );
}

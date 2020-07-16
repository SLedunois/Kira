import React from 'react';
import {Modal} from "@ui/Modal";
import {Project} from "../../types";
import {Trans, useTranslation} from "react-i18next";

type IDeletionModal = {
  project: Project
  show: boolean
  onClose: any
  onValidation: any
}

export const DeletionModal = ({project, show, onClose, onValidation}: IDeletionModal) => {
  const {t} = useTranslation();

  return (
    <Modal active={show} title={t('delete a project')} validationLabel={t('yes, delete project')} onClose={onClose}
           onValidation={() => onValidation(project)}>
      <div className="p-4">
        <p>
          <Trans t={t}>deleting a project will permanently remove it and all its associated information</Trans>.
          &nbsp;
          <span className="font-bold text-secondary-100">
            <Trans t={t}>this can not be undone</Trans>.
          </span>
        </p>
        <p className="mt-4"><Trans t={t}>are sur you want to delete project</Trans> <span
          className="font-bold">{project.name}</span> ?</p>
      </div>
    </Modal>
  )
}

import React from 'react';
import {Modal} from "../../../../components/Modal";
import {Project} from "../../types";

type IDeletionModal = {
  project: Project
  show: boolean
  onClose: any
  onValidation: any
}

export const DeletionModal = ({project, show, onClose, onValidation}: IDeletionModal) => (
  <Modal active={show} title="Delete a project" validationLabel="Yes, delete project" onClose={onClose}
         onValidation={() => onValidation(project)}>
    <div className="p-4">
      <p>
        Deleting a project will permanently remove it and all its associated information.
        &nbsp;<span className="font-bold text-secondary-100">This can not be undone.</span>
      </p>
      <p className="mt-4">Are sur you want to delete project <span className="font-bold">{project.name}</span>.</p>
    </div>
  </Modal>
);

import http from 'axios';
import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {Project} from './types';
import {randomColor} from "../../utils";

type ProjectState = {
  loading: boolean
  projects: Project[]
}

const initialState: ProjectState = {
  loading: false,
  projects: []
}

function initProject(project: Project) {
  if (project.members.length > 0) project.members.map(member => member.color = randomColor());
}

export const fetchProjects = createAsyncThunk(
  'projects/fetch',
  async (sort?: number) => {
    const {data} = await http.get(`/api/v1/projects${sort ? `?sort=${sort}` : ''}`);
    data.forEach(initProject)
    return data;
  }
);

export const createProjects = createAsyncThunk(
  'projects/create',
  async (project: Project) => {
    const {data} = await http.post('/api/v1/projects', project);
    return data;
  }
);

export const updateProject = createAsyncThunk(
  'projects/update',
  async (project: Project) => {
    const {data} = await http.put(`/api/v1/projects/${project.id}`, project);
    return data;
  }
);

export const deleteProject = createAsyncThunk(
  'projects/delete',
  async (project: Project) => {
    const {data} = await http.delete(`/api/v1/projects/${project.id}`);
    return data;
  }
)

const projectSlice = createSlice({
  name: 'project',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchProjects.pending.toString()]: state => {
      state.loading = true;
    },
    [fetchProjects.fulfilled.toString()]: (state, action) => {
      state.loading = false;
      state.projects = action.payload;
    },
    [fetchProjects.rejected.toString()]: state => {
      state.loading = false;
    }
  }
});

export default projectSlice.reducer;

import {Member} from "../projects/types";

export type ITicket = {
  id?: number
  name: string
  content: string
  assignee: Member
  index?: number
  owner: string
  activity_id: number
}

export type IActivity = {
  id: number
  name: string
  position: number
  tickets: ITicket[]
}

export type ITicket = {
  id: number
  name: string
  content: string
  assignee: string
  index: number
  activity_id: number
}

export type IActivity = {
  id: number
  name: string
  position: number
  tickets: ITicket[]
}

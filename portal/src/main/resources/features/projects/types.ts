export type Project = {
  id?: number
  name: string
  owner?: string
  created?: string
  members: Member[]
}

export type Member = {
  email: string
  first_name: string
  last_name: string
  color?: string
}

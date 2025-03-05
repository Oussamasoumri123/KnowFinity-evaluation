enum RoleType {
  ROLE_USER,
  ROLE_ADMIN,
  ROLE_STUDENT,
}
export interface Role {
  id?: number;
  name?: RoleType;
  permission?: string;
}

export class User {
  constructor(
    public email: string,
    public userId: string,
    public firstName: string,
    public lastName: string,
    public role: string,
    public jwtToken: string
  ) {}
}

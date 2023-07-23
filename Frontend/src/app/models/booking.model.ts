import { User } from "./user.model";

export class Booking{
    constructor(
        public id: string,
        public show:string,
        public user:User,
        public numSeats:number,
        public status:string
      ) {}
}
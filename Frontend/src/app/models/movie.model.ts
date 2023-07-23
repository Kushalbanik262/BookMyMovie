import { show } from "./show.model";

export class Movie {
  constructor(
    public id: string,
    public title: string,
    public description: string,
    public releaseDate: string,
    public runtime: number,
    public genre: string,
    public language: string,
    public country: string,
    public director: string,
    public cast: string,
    public rating: number,
    public posterUrl: string,
    public trailerUrl: string,
    public shows: show[]
  ) {}
}

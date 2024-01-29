import {Injectable} from "@angular/core";
import {BehaviorSubject, firstValueFrom, Observable} from "rxjs";
import {Message} from "./message.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
	providedIn: "root",
})
export class MessagesService {
	messages = new BehaviorSubject<Message[]>([]);

	constructor(private httpClient: HttpClient) {
	}

	async postMessage(message: Message): Promise<void> {
		const response = await firstValueFrom(
			this.httpClient.post(
				`${environment.backendUrl}/messages`,
				{
					id: message.id,
					username: message.username,
					timestamp: message.timestamp,
					text: message.text
				}
			)
		)
		// const currentMessages = this.messages.value; // Récupérer le tab actuel des msgs à partir du BehaviorSubject
		// const updatedMessage = [...currentMessages, message]  // Ajouter le nouveau msg au tab existant
		// this.messages.next(updatedMessage) //Emettre nouveau tab de msg updaté
		this.messages.next([...this.messages.value, message])
		// console.log(response)
	}

	getMessages(): Observable<Message[]> {
		const response = firstValueFrom(
			this.httpClient.get<Message[]>(
				`${environment.backendUrl}/messages`,
				{}
			)
		)
		response.then(list => this.messages.next(list));
		return this.messages.asObservable();
	}

}

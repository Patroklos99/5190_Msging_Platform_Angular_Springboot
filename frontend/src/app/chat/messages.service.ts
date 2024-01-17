import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import {Message} from "./message.model";

@Injectable({
	providedIn: "root",
})
export class MessagesService {
	messages = new BehaviorSubject<Message[]>([]);

	constructor() {
	}

	postMessage(message: Message): void {
		const currentMessages = this.messages.value; // Récupérer le tab actuel des msgs à partir du BehaviorSubject
		const updatedMessage = [...currentMessages, message]  // Ajouter le nouveau msg au tab existant
		this.messages.next(updatedMessage) //Emettre le nouvea tab de msg updaté
	}

	getMessages(): Observable<Message[]> {
		return this.messages.asObservable();
	}
}

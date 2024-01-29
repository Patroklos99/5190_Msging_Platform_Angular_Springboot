import {Injectable, OnDestroy} from "@angular/core";
import {BehaviorSubject, firstValueFrom, Observable} from "rxjs";
import {Message} from "./message.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {WebsocketService} from "../app.WebsocketService";

@Injectable({
	providedIn: "root",
})
export class MessagesService implements OnDestroy{
	messages = new BehaviorSubject<Message[]>([]);

	constructor(private httpClient: HttpClient, private webSocket: WebsocketService) {
		this.connectToWebSocket();
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
	}

	ngOnDestroy() : void {
		if (this.webSocket) {
			this.webSocket.disconnect()
		}
	}

	getMessages(): Observable<Message[]> {
		return this.messages.asObservable();
	}

	private connectToWebSocket() {
		this.webSocket.connect().subscribe((data) => {
			if (data === "notif") {
				this.refreshMessages()
			}
		})
	}

	private refreshMessages() {
		const response = firstValueFrom(
			this.httpClient.get<Message[]>(
				`${environment.backendUrl}/messages`,
				{}
			)
		)
		response.then(list => this.messages.next(list));
	}
}

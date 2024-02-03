import {Injectable, OnDestroy} from "@angular/core";
import {BehaviorSubject, firstValueFrom, Observable} from "rxjs";
import {Message, MessageRequest} from "./message.model";
import {HttpClient, HttpParams} from "@angular/common/http";
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

	async postMessage(message: MessageRequest): Promise<void> {
		const response = await firstValueFrom(
			this.httpClient.post(
				`${environment.backendUrl}/messages`,
				{
					username: message.username,
					text: message.text,
					imageData: message.imageData
				}
			)
		)
	}

	ngOnDestroy() : void {
		if (this.webSocket) {
			this.webSocket.disconnect()
		}
	}

	private connectToWebSocket() {
		this.webSocket.connect().subscribe((data) => {
			if (data === "notif") {
				this.refreshMessages()
			}
		})
	}

	getMessages(): Observable<Message[]> {
		return this.messages.asObservable();
	}

	refreshMessages() {
		const lastMsg = this.messages.value[this.messages.value.length-1];
		let params = new HttpParams();
		if (lastMsg) {
			params = params.append("fromId", lastMsg.id!.toString());
		}

		const response = firstValueFrom(
			this.httpClient.get<Message[]>(
				`${environment.backendUrl}/messages`,
				{params}
			)
		);
		response.then(list => this.messages.next([...this.messages.value, ...list]));
	}

	clearMessages() {
		this.messages.next([]);
	}

}

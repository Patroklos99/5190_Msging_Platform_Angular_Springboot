import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {environment} from '../environments/environment';

@Injectable({
	providedIn: 'root',
})
export class WebsocketService {
	private ws: WebSocket | null = null;
	private events: Subject<'notif'> | null = null;

	constructor() {
	}

	public connect(): Observable<'notif'> {
		try {
			this.ws = new WebSocket(`${environment.wsUrl}/notifications`);
		} catch (e) {
			setTimeout(() => {
				this.connect();
			}, 2000);
		}

		if (!this.events) {
			this.events = new Subject<'notif'>();
		}

		this.ws = new WebSocket(`${environment.wsUrl}/notifications`);
		// const events = new Subject<'notif'>();
		this.ws.onmessage = () => this.events?.next('notif');
		this.ws.onclose = () => {
			this.events?.complete();
			this.retryConnection();
		}
		this.ws.onerror = () => {
			this.events?.error('error');
			this.retryConnection();
		};
		return this.events.asObservable();
	}

	public disconnect() {
		if (this.ws)
			this.ws?.close();
		this.ws = null;
	}

	private retryConnection() {
		setTimeout(() => {
			this.ws = null;
			this.connect();
		}, 2000)
	}
}
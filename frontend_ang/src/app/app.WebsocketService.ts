import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {environment} from '../environments/environment';

@Injectable({
	providedIn: 'root',
})
export class WebsocketService {
	private ws: WebSocket | null = null;

	constructor() {
	}

	public connect(): Observable<'notif'> {
		this.ws = new WebSocket(`${environment.wsUrl}/notifications`);
		const events = new Subject<'notif'>();
		this.ws.onmessage = () => events.next('notif');
		this.ws.onclose = () => events.complete();
		this.ws.onerror = () => events.error('error');
		return events.asObservable();
	}

	public disconnect() {
		this.ws?.close();
		this.ws = null;
	}
}
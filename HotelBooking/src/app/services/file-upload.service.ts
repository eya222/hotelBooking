import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { ProcessHttpmsgService } from './process-httpmsg.service';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  private baseUrl = 'http://localhost:8085'; // Adjust if your backend URL is different

  constructor(
    private http: HttpClient,
    private processHTTPMsgService: ProcessHttpmsgService
  ) { }
   
  // Uploads an image file to the server with the specified room number
  upload(formData: FormData, roomNumber: number): Observable<HttpEvent<any>> {
    // Construct the request to the updated backend endpoint
    const req = new HttpRequest('POST', `${this.baseUrl}/api/storage/upload/${roomNumber}`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }
  
} 

import { Component, OnInit } from '@angular/core';
import { ImageService } from '../../services/image.service';

@Component({
  selector: 'app-image-grid',
  templateUrl: './image-grid.component.html',
  styleUrls: ['./image-grid.component.css']
})
export class ImageGridComponent implements OnInit {
  images: any[] = [];
  selectedImageId: number | null = null;

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {
    this.loadImages();
  }

  loadImages() {
    this.imageService.getImages().subscribe(data => this.images = data);
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.imageService.uploadImage(file).subscribe(() => this.loadImages());
    }
  }

  selectImage(id: number) {
    this.selectedImageId = id;
  }

  deleteSelectedImage() {
    if (this.selectedImageId !== null) {
      this.imageService.deleteImage(this.selectedImageId).subscribe(() => {
        this.selectedImageId = null;
        this.loadImages();
      });
    }
  }
}
